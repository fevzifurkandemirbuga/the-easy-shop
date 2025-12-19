# Store API – Controllers

This project exposes REST endpoints for **Categories**, **Products**, **Shopping Cart**, **Profile**, and **Orders**.


## Authentication & Authorization (Summary)

This API uses Spring Security.

- **Public endpoints** (no login needed):
  - `GET /products`
  - `GET /products/{id}`
  - `GET /categories`
  - `GET /categories/{id}`
  - `GET /categories/{categoryId}/products`
- **Authenticated endpoints** (must be logged in):
  - `/cart/**`
  - `/profile/**`
  - `POST /orders`
- **Admin-only endpoints**:
  - `POST /categories`
  - `PUT /categories/{id}`
  - `DELETE /categories/{id}`
  - `POST /products`
  - `PUT /products/{id}`
  - `DELETE /products/{id}`
    
---

# 1) CategoriesController

**Base path:** `/categories`

### Endpoints

#### Get all categories
- `GET /categories`
- **Auth:** Public
- **Returns:** `List<Category>`

#### Get category by id
- `GET /categories/{id}`
- **Auth:** Public
- **Returns:** `Category`
- **Errors:** `404` if category not found

#### Get products by category id
- `GET /categories/{categoryId}/products`
- **Auth:** Public
- **Returns:** `List<Product>`

#### Add category (Admin)
- `POST /categories`
- **Auth:** Admin only
- **Status:** `201 Created`
- **Body:** `Category`
- **Returns:** created `Category`

#### Update category (Admin)
- `PUT /categories/{id}`
- **Auth:** Admin only
- **Body:** `Category`
- **Returns:** `void`

#### Delete category (Admin)
- `DELETE /categories/{id}`
- **Auth:** Admin only
- **Status:** `204 No Content`
- **Returns:** `void`

---
# 2) ProductsController

**Base path:** `/products`

### Endpoints

#### Search products
- `GET /products`
- **Auth:** Public
- **Query params (optional):**
  - `cat` (category id)
  - `minPrice`
  - `maxPrice`
  - `subCategory`
- **Returns:** `List<Product>`

#### Get product by id
- `GET /products/{id}`
- **Auth:** Public
- **Returns:** `Product`
- **Errors:** `404` if product not found

#### Add product (Admin)
- `POST /products`
- **Auth:** Admin only
- **Body:** `Product`
- **Returns:** created `Product`

#### Update product (Admin)
- `PUT /products/{id}`
- **Auth:** Admin only
- **Body:** `Product`
- **Returns:** `void`

#### Delete product (Admin)
- `DELETE /products/{id}`
- **Auth:** Admin only
- **Returns:** `void`
- **Errors:** `404` if product not found

---
# 3) ShoppingCartController

**Base path:** `/cart`

### Endpoints

#### Get current user's cart
- `GET /cart`
- **Auth:** Logged in users only
- **Returns:** `ShoppingCart`

#### Add product to cart
- `POST /cart/products/{id}`
- **Auth:** Logged in users only
- **Status:** `201 Created`
- **Returns:** `ShoppingCart`

#### Update product quantity in cart
- `PUT /cart/products/{id}`
- **Auth:** Logged in users only
- **Body:** `QuantityDto`
- **Returns:** `ShoppingCart`

#### Clear cart
- `DELETE /cart`
- **Auth:** Logged in users only
- **Returns:** `void`

---
# 4) ProfileController

**Base path:** `/profile`

### Endpoints

#### Get current user's profile
- `GET /profile`
- **Auth:** Logged in users only
- **Returns:** `Profile`

#### Update current user's profile
- `PUT /profile`
- **Auth:** Logged in users only
- **Body:** `Profile`
- **Returns:** updated `Profile`

---
# 5) OrdersController

**Base path:** `/orders`

### Endpoints

#### Create order
- `POST /orders`
- **Auth:** Logged in users only
- **Status:** `201 Created`
- **Body:** none
- **Returns:** `Order`


