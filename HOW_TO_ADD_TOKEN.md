# How to Add Token in Swagger UI - Visual Guide

## Quick Answer

After logging in and getting your JWT token, here's exactly how to add it in Swagger:

### Step-by-Step with Screenshots

#### Step 1: Login and Copy Token

1. In Swagger UI, go to `POST /api/auth/login`
2. Click "Try it out"
3. Enter your credentials and click "Execute"
4. In the response, you'll see something like this:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTczNDEzODI0MSwiZXhwIjoxNzM0MjI0NjQxfQ.BgfTg1uDaBtpTNIWpJYexirRF19Hu4OUwJRXS8rY1Tc",
  "id": null,
  "username": "testuser",
  "email": null,
  "type": "Bearer"
}
```

5. **Select and copy the ENTIRE token value** (the long string after `"token":`)
   - Start from `eyJ...` 
   - Copy all the way to the end
   - Do NOT include the quotes

---

#### Step 2: Find the Authorize Button

Look at the **top of the Swagger UI page**, you'll see:

```
Budget Tracker API                    [Authorize 🔒]
```

The **Authorize** button is on the right side with a lock icon 🔒

---

#### Step 3: Click Authorize

Click the **"Authorize"** button. A popup window will appear titled:

```
Available authorizations
```

You'll see:

```
bearerAuth (http, Bearer)
Description: Enter JWT token obtained from /api/auth/login

Value: [                                    ]
      ↑ Paste your token here
```

---

#### Step 4: Paste Your Token

1. Click in the **"Value"** text field
2. **Paste your token** (Ctrl+V or Cmd+V)
3. Your token should look like:
   ```
   eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTczNDEzODI0MSwiZXhwIjoxNzM0MjI0NjQxfQ.BgfTg1uDaBtpTNIWpJYexirRF19Hu4OUwJRXS8rY1Tc
   ```

**IMPORTANT:** 
- ✅ **DO** paste just the token
- ❌ **DON'T** add "Bearer" before it
- ❌ **DON'T** include the quotes
- ❌ **DON'T** add any spaces

---

#### Step 5: Authorize

1. Click the **"Authorize"** button in the popup
2. You should see a checkmark ✓ appear
3. Click **"Close"** to close the popup

---

#### Step 6: Verify Authorization

After closing the popup, look at the Authorize button again:

```
Budget Tracker API                    [Logout 🔓]
```

- The lock icon should now be **unlocked** 🔓
- The button text changed from "Authorize" to "Logout"
- This means you're successfully authorized!

---

## Visual Example

```
┌─────────────────────────────────────────────────────┐
│  Available authorizations                      [X]  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  bearerAuth (http, Bearer)                         │
│  Description: Enter JWT token obtained from        │
│               /api/auth/login                      │
│                                                     │
│  Value:                                            │
│  ┌───────────────────────────────────────────────┐ │
│  │ eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNl │ │
│  │ ciIsImlhdCI6MTczNDEzODI0MSwiZXhwIjoxNzM0M │ │
│  │ jI0NjQxfQ.BgfTg1uDaBtpTNIWpJYexirRF19Hu4O │ │
│  │ UwJRXS8rY1Tc                                │ │
│  └───────────────────────────────────────────────┘ │
│                                                     │
│              [Authorize]  [Close]                  │
└─────────────────────────────────────────────────────┘
```

---

## Testing It Works

After authorizing, try any protected endpoint:

1. Go to `GET /api/categories`
2. Click "Try it out"
3. Click "Execute"
4. You should get a **200 OK** response (not 401 Unauthorized)

If you get **401 Unauthorized**, it means:
- Token wasn't pasted correctly
- Token expired (login again to get a new one)
- You forgot to click "Authorize" in the popup

---

## Token Expiration

Your token expires after **24 hours**. When it expires:

1. You'll start getting **401 Unauthorized** errors
2. Simply login again to get a new token
3. Click "Logout" (the unlocked lock 🔓)
4. Paste the new token and authorize again

---

## Quick Troubleshooting

### ❌ Getting 401 Unauthorized?

**Check:**
- Did you click the "Authorize" button in the popup?
- Did you paste the entire token (no quotes, no "Bearer")?
- Is the lock icon unlocked 🔓?
- Has your token expired? (login again)

### ❌ Can't find the Authorize button?

- It's at the **very top right** of the page
- Look for the lock icon 🔒
- Scroll to the top of the Swagger UI page

### ❌ Token looks wrong?

A valid token has **3 parts separated by dots**:
```
part1.part2.part3
eyJhbG...  .  eyJzdWI...  .  BgfTg1u...
```

If your token doesn't have 3 parts, you copied it wrong.

---

## Summary: The Complete Flow

```
1. Login → Get token from response
2. Copy the entire token (eyJ...)
3. Click "Authorize" button (top right, 🔒)
4. Paste token in "Value" field
5. Click "Authorize" in popup
6. Click "Close"
7. Lock icon changes to 🔓
8. Now you can test all endpoints!
```

---

## Need More Help?

See the complete testing guide: [SWAGGER_GUIDE.md](SWAGGER_GUIDE.md)
