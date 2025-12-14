package com.example.budgetapp.service.impl;

import com.example.budgetapp.dto.BudgetDto;
import com.example.budgetapp.entity.Budget;
import com.example.budgetapp.entity.Category;
import com.example.budgetapp.entity.User;
import com.example.budgetapp.exception.APIException;
import com.example.budgetapp.exception.ResourceNotFoundException;
import com.example.budgetapp.mapper.BudgetMapper;
import com.example.budgetapp.repository.BudgetRepository;
import com.example.budgetapp.repository.CategoryRepository;
import com.example.budgetapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BudgetService Tests")
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private User testUser;
    private Category testCategory;
    private Budget testBudget;
    private BudgetDto.Request budgetRequest;
    private BudgetDto.Response budgetResponse;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Setup test category
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Groceries");
        testCategory.setUser(testUser);

        // Setup test budget
        testBudget = new Budget();
        testBudget.setId(1L);
        testBudget.setAmount(new BigDecimal("500.00"));
        testBudget.setMonth(YearMonth.of(2024, 1));
        testBudget.setCategory(testCategory);

        // Setup DTOs
        budgetRequest = new BudgetDto.Request();
        budgetRequest.setCategoryId(1L);
        budgetRequest.setAmount(new BigDecimal("500.00"));
        budgetRequest.setMonth(YearMonth.of(2024, 1));

        budgetResponse = new BudgetDto.Response();
        budgetResponse.setId(1L);
        budgetResponse.setAmount(new BigDecimal("500.00"));
        budgetResponse.setMonth(YearMonth.of(2024, 1));

        // Setup security context
        lenient().when(userDetails.getUsername()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null));
    }

    @Test
    @DisplayName("Should create budget successfully")
    void createBudget_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(budgetRepository.findByCategoryIdAndMonth(1L, YearMonth.of(2024, 1)))
                .thenReturn(Optional.empty());
        when(budgetMapper.toEntity(budgetRequest)).thenReturn(testBudget);
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);
        when(budgetMapper.toResponse(testBudget)).thenReturn(budgetResponse);

        // Act
        BudgetDto.Response result = budgetService.createBudget(budgetRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void createBudget_CategoryNotFound() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> budgetService.createBudget(budgetRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Category");
    }

    @Test
    @DisplayName("Should throw exception when category doesn't belong to user")
    void createBudget_CategoryNotBelongToUser() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        testCategory.setUser(otherUser);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));

        // Act & Assert
        assertThatThrownBy(() -> budgetService.createBudget(budgetRequest))
                .isInstanceOf(APIException.class)
                .hasMessageContaining("Category does not belong to user");
    }

    @Test
    @DisplayName("Should throw exception when budget already exists for category and month")
    void createBudget_BudgetAlreadyExists() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(budgetRepository.findByCategoryIdAndMonth(1L, YearMonth.of(2024, 1)))
                .thenReturn(Optional.of(testBudget));

        // Act & Assert
        assertThatThrownBy(() -> budgetService.createBudget(budgetRequest))
                .isInstanceOf(APIException.class)
                .hasMessageContaining("Budget already exists");
    }

    @Test
    @DisplayName("Should get budget by ID successfully")
    void getBudgetById_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(testBudget));
        when(budgetMapper.toResponse(testBudget)).thenReturn(budgetResponse);

        // Act
        BudgetDto.Response result = budgetService.getBudgetById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(budgetRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when budget not found")
    void getBudgetById_NotFound() {
        // Arrange
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> budgetService.getBudgetById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Budget");
    }

    @Test
    @DisplayName("Should get all budgets for user")
    void getAllBudgets_Success() {
        // Arrange
        Budget budget2 = new Budget();
        budget2.setId(2L);
        budget2.setAmount(new BigDecimal("300.00"));
        budget2.setCategory(testCategory);

        BudgetDto.Response response2 = new BudgetDto.Response();
        response2.setId(2L);
        response2.setAmount(new BigDecimal("300.00"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(budgetRepository.findByCategoryUserId(1L))
                .thenReturn(Arrays.asList(testBudget, budget2));
        when(budgetMapper.toResponse(testBudget)).thenReturn(budgetResponse);
        when(budgetMapper.toResponse(budget2)).thenReturn(response2);

        // Act
        List<BudgetDto.Response> results = budgetService.getAllBudgets();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getId()).isEqualTo(1L);
        assertThat(results.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Should update budget successfully")
    void updateBudget_Success() {
        // Arrange
        BudgetDto.Request updateRequest = new BudgetDto.Request();
        updateRequest.setAmount(new BigDecimal("600.00"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(testBudget));
        when(budgetRepository.save(any(Budget.class))).thenReturn(testBudget);
        when(budgetMapper.toResponse(testBudget)).thenReturn(budgetResponse);

        // Act
        BudgetDto.Response result = budgetService.updateBudget(1L, updateRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(budgetRepository, times(1)).save(testBudget);
        assertThat(testBudget.getAmount()).isEqualByComparingTo(new BigDecimal("600.00"));
    }

    @Test
    @DisplayName("Should delete budget successfully")
    void deleteBudget_Success() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(testBudget));
        doNothing().when(budgetRepository).delete(testBudget);

        // Act
        budgetService.deleteBudget(1L);

        // Assert
        verify(budgetRepository, times(1)).delete(testBudget);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent budget")
    void deleteBudget_NotFound() {
        // Arrange
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> budgetService.deleteBudget(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Budget");
    }
}
