#!/bin/bash

# Budget Tracker API Endpoint Verification Script
# This script tests all major endpoints of the Budget Tracker API

BASE_URL="http://localhost:8080"
echo "========================================="
echo "Budget Tracker API Endpoint Tests"
echo "========================================="
echo ""

# Test 1: Health Check
echo "1. Testing Health Endpoint..."
HEALTH=$(curl -s ${BASE_URL}/actuator/health)
echo "Response: $HEALTH"
echo ""

# Test 2: Register User
echo "2. Testing User Registration..."
REGISTER=$(curl -s -X POST ${BASE_URL}/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"apitest","email":"apitest@example.com","password":"test123"}')
echo "Response: $REGISTER"
echo ""

# Test 3: Login
echo "3. Testing User Login..."
LOGIN_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"apitest","password":"test123"}')
echo "Response: $LOGIN_RESPONSE"

# Extract token
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token extracted: ${TOKEN:0:50}..."
echo ""

# Test 4: Create Category
echo "4. Testing Create Category..."
CATEGORY=$(curl -s -X POST ${BASE_URL}/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Food","type":"EXPENSE"}')
echo "Response: $CATEGORY"
echo ""

# Test 5: Get All Categories
echo "5. Testing Get All Categories..."
CATEGORIES=$(curl -s -X GET ${BASE_URL}/api/categories \
  -H "Authorization: Bearer $TOKEN")
echo "Response: $CATEGORIES"
echo ""

# Test 6: Create Budget
echo "6. Testing Create Budget..."
BUDGET=$(curl -s -X POST ${BASE_URL}/api/budgets \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"amount":1000.00,"month":"2024-12","categoryId":1}')
echo "Response: $BUDGET"
echo ""

# Test 7: Get All Budgets
echo "7. Testing Get All Budgets..."
BUDGETS=$(curl -s -X GET ${BASE_URL}/api/budgets \
  -H "Authorization: Bearer $TOKEN")
echo "Response: $BUDGETS"
echo ""

# Test 8: Create Account
echo "8. Testing Create Account..."
ACCOUNT=$(curl -s -X POST ${BASE_URL}/api/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"Checking Account","type":"CHECKING","balance":5000.00}')
echo "Response: $ACCOUNT"
echo ""

# Test 9: Get All Accounts
echo "9. Testing Get All Accounts..."
ACCOUNTS=$(curl -s -X GET ${BASE_URL}/api/accounts \
  -H "Authorization: Bearer $TOKEN")
echo "Response: $ACCOUNTS"
echo ""

# Test 10: Create Transaction
echo "10. Testing Create Transaction..."
TRANSACTION=$(curl -s -X POST ${BASE_URL}/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"amount":50.00,"description":"Grocery shopping","date":"2024-12-13","type":"EXPENSE","categoryId":1,"accountId":1}')
echo "Response: $TRANSACTION"
echo ""

# Test 11: Get All Transactions
echo "11. Testing Get All Transactions..."
TRANSACTIONS=$(curl -s -X GET ${BASE_URL}/api/transactions \
  -H "Authorization: Bearer $TOKEN")
echo "Response: $TRANSACTIONS"
echo ""

echo "========================================="
echo "All endpoint tests completed!"
echo "========================================="
