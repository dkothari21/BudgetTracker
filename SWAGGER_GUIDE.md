# Swagger UI Testing Guide for Budget Tracker API

## What is Swagger UI?

Swagger UI is an interactive web interface that lets you test API endpoints directly in your browser without writing any code or using external tools like Postman.

## Prerequisites

- Application running on http://localhost:8080
- A web browser (Chrome, Firefox, Safari, etc.)

> 🔑 **Need help adding the token?** See the detailed visual guide: [HOW_TO_ADD_TOKEN.md](HOW_TO_ADD_TOKEN.md)

## Step-by-Step Testing Guide

### Step 1: Start the Application

Open your terminal and run:
```bash
./gradlew bootRun
```

Wait for the message: `Started BudgetAppApplication in X seconds`

### Step 2: Open Swagger UI

Open your browser and go to:
```
http://localhost:8080/swagger-ui/index.html
```

You should see a page titled **"Budget Tracker API"** with a list of endpoints organized by controllers.

---

## Complete Testing Flow (First Time User)

### Phase 1: Create Your Account

#### 1.1 Register a New User

1. **Find the auth-controller section** (it's at the top)
2. **Click on `POST /api/auth/register`** to expand it
3. **Click the "Try it out" button** (top right of the section)
4. **Edit the Request Body** with your details:
   ```json
   {
     "username": "myusername",
     "email": "myemail@example.com",
     "password": "mypassword123"
   }
   ```
   > 💡 **Tip:** You can use any username/email/password you want. Remember these for login!

5. **Click the blue "Execute" button**
6. **Check the Response:**
   - Look for **Response Code: 201** (Created)
   - Response body should say: `"User registered successfully!."`

✅ **Success!** Your account is created.

---

#### 1.2 Login to Get Your Access Token

1. **Click on `POST /api/auth/login`** (just below register)
2. **Click "Try it out"**
3. **Enter your credentials:**
   ```json
   {
     "username": "myusername",
     "password": "mypassword123"
   }
   ```
   > ⚠️ **Important:** Use the SAME username and password you just registered with!

4. **Click "Execute"**
5. **Check the Response:**
   - Response Code: **200** (OK)
   - You'll see a JSON response with a `"token"` field
   
6. **Copy the Token:**
   - Find the long string after `"token":` (it starts with `eyJ...`)
   - Select and copy the ENTIRE token (without the quotes)
   - Example: `eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciI...`

✅ **Success!** You now have your authentication token.

---

### Phase 2: Authorize Swagger UI

This step tells Swagger to include your token in all future requests.

1. **Find the "Authorize" button** at the top of the page (it has a lock icon 🔒)
2. **Click "Authorize"**
3. **A popup will appear** with a field labeled "Value"
4. **Paste your token** into the "Value" field
   - Just paste the token directly (no "Bearer" prefix needed)
5. **Click "Authorize"** button in the popup
6. **Click "Close"** to close the popup

✅ **Success!** You're now authenticated. The lock icon should now be closed 🔒.

---

### Phase 3: Test Protected Endpoints

Now you can test any endpoint! Let's create a complete budget workflow.

#### 3.1 Create a Category

Categories are used to organize your budgets (e.g., "Groceries", "Rent", "Entertainment").

1. **Find `category-controller`**
2. **Click on `POST /api/categories`**
3. **Click "Try it out"**
4. **Enter category details:**
   ```json
   {
     "name": "Groceries",
     "type": "EXPENSE"
   }
   ```
   > 💡 **Type options:** `EXPENSE` or `INCOME`

5. **Click "Execute"**
6. **Check Response:**
   - Response Code: **201** (Created)
   - Note the `"id"` in the response (e.g., `"id": 1`)
   - **Remember this ID** - you'll need it for the budget!

✅ **Success!** Category created.

---

#### 3.2 Create a Budget

Now let's set a budget for your category.

1. **Find `budget-controller`**
2. **Click on `POST /api/budgets`**
3. **Click "Try it out"**
4. **Enter budget details:**
   ```json
   {
     "amount": 500.00,
     "month": "2024-12",
     "categoryId": 1
   }
   ```
   > ⚠️ **Important:** Use the category ID from step 3.1!
   > 💡 **Month format:** YYYY-MM (e.g., "2024-12" for December 2024)

5. **Click "Execute"**
6. **Check Response:**
   - Response Code: **201** (Created)
   - You'll see your budget with the amount and category name

✅ **Success!** Budget created.

---

#### 3.3 View All Your Budgets

1. **Click on `GET /api/budgets`**
2. **Click "Try it out"**
3. **Click "Execute"** (no parameters needed)
4. **Check Response:**
   - Response Code: **200** (OK)
   - You'll see a list of all your budgets

✅ **Success!** You can see all your budgets.

---

#### 3.4 Create an Account

Accounts represent your bank accounts, wallets, etc.

1. **Find `account-controller`**
2. **Click on `POST /api/accounts`**
3. **Click "Try it out"**
4. **Enter account details:**
   ```json
   {
     "name": "Checking Account",
     "type": "CHECKING",
     "balance": 5000.00
   }
   ```
   > 💡 **Type options:** `CHECKING`, `SAVINGS`, `CREDIT_CARD`, `CASH`

5. **Click "Execute"**
6. **Check Response:**
   - Response Code: **201** (Created)
   - Note the account `"id"` (you'll need this for transactions)

✅ **Success!** Account created.

---

#### 3.5 Create a Transaction

Record an actual expense or income.

1. **Find `transaction-controller`**
2. **Click on `POST /api/transactions`**
3. **Click "Try it out"**
4. **Enter transaction details:**
   ```json
   {
     "amount": 50.00,
     "description": "Weekly grocery shopping",
     "date": "2024-12-13",
     "type": "EXPENSE",
     "categoryId": 1,
     "accountId": 1
   }
   ```
   > ⚠️ **Important:** Use the category ID and account ID from previous steps!
   > 💡 **Type options:** `EXPENSE` or `INCOME`
   > 💡 **Date format:** YYYY-MM-DD

5. **Click "Execute"**
6. **Check Response:**
   - Response Code: **201** (Created)
   - You'll see your transaction details

✅ **Success!** Transaction recorded.

---

#### 3.6 View All Transactions

1. **Click on `GET /api/transactions`**
2. **Click "Try it out"**
3. **Click "Execute"**
4. **Check Response:**
   - Response Code: **200** (OK)
   - You'll see all your transactions

✅ **Success!** You can see your transaction history.

---

## Testing Other Operations

### Update Operations (PUT)

1. Find any `PUT` endpoint (e.g., `PUT /api/budgets/{id}`)
2. Click "Try it out"
3. Enter the **ID** of the item you want to update
4. Modify the request body with new values
5. Click "Execute"

### Delete Operations (DELETE)

1. Find any `DELETE` endpoint (e.g., `DELETE /api/budgets/{id}`)
2. Click "Try it out"
3. Enter the **ID** of the item you want to delete
4. Click "Execute"

### Get by ID (GET with parameter)

1. Find any `GET /{id}` endpoint (e.g., `GET /api/budgets/{id}`)
2. Click "Try it out"
3. Enter the **ID** you want to retrieve
4. Click "Execute"

---

## Understanding Response Codes

| Code | Meaning | What It Means |
|------|---------|---------------|
| **200** | OK | Request successful, data returned |
| **201** | Created | New resource created successfully |
| **204** | No Content | Successful deletion |
| **400** | Bad Request | Invalid data sent (check your JSON) |
| **401** | Unauthorized | Token missing or invalid (re-authorize) |
| **404** | Not Found | Resource with that ID doesn't exist |
| **500** | Server Error | Something went wrong on the server |

---

## Common Issues & Solutions

### ❌ "401 Unauthorized" Error

**Problem:** Your token expired or wasn't set correctly.

**Solution:**
1. Go back to Step 1.2 (Login)
2. Get a new token
3. Click "Authorize" again and paste the new token

---

### ❌ "Category does not belong to user"

**Problem:** You're trying to use a category created by a different user.

**Solution:**
1. Create your own category first (Step 3.1)
2. Use that category's ID in your budget/transaction

---

### ❌ "404 Not Found"

**Problem:** The ID you entered doesn't exist.

**Solution:**
1. Use `GET` endpoints to see all items and their IDs
2. Copy the correct ID from the response
3. Try again with the correct ID

---

### ❌ "400 Bad Request - Validation Failed"

**Problem:** Your JSON has invalid data (wrong format, missing required fields).

**Solution:**
1. Check the error message for details
2. Common issues:
   - Missing required fields (username, email, password, etc.)
   - Invalid email format
   - Invalid date format (should be YYYY-MM-DD)
   - Invalid month format (should be YYYY-MM)
   - Negative amounts

---

## Quick Reference: Complete Testing Flow

```
1. Register User → Copy username/password
2. Login → Copy token
3. Authorize → Paste token
4. Create Category → Copy category ID
5. Create Budget → Use category ID
6. Create Account → Copy account ID
7. Create Transaction → Use category ID + account ID
8. View All → See your data
```

---

## Tips for Success

✅ **Always authorize first** after logging in  
✅ **Keep track of IDs** - write them down or copy them  
✅ **Check response codes** - 200/201 means success  
✅ **Read error messages** - they tell you what's wrong  
✅ **Use GET endpoints** to see what data exists  
✅ **Test in order** - create categories before budgets  

---

## Need Help?

- Check the **Response** section after each request for error details
- Make sure you're **authorized** (lock icon should be closed 🔒)
- Verify your **JSON format** is correct (use the examples above)
- Ensure you're using **valid IDs** from previously created items

Happy Testing! 🎉
