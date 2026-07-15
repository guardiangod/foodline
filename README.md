# FoodLine (Backend Service)

FoodLine is a Spring Boot backend for a **food + grocery delivery** platform.

This project intentionally keeps the domain small, but implements the key rules:

* **Two catalog types**: `GROCERY` (inventory-driven) and `FOOD` (menu-driven)
* **One cart = one outlet + one catalog type**
  * If you try to add an item from a different outlet/type, the API returns **409** and asks you to confirm an override.
* **Checkout** creates an order and performs fulfillment:
  * `GROCERY`: reduces stock **only** in the assigned store
  * `FOOD`: **no stock updates**

---

## Tech Stack

* Java 21
* Spring Boot 3
* Spring Data JPA
* H2 (in-memory)
* Gradle

---

## Running locally

If you have the Gradle wrapper (`gradlew`) in your environment, use it:

```bash
./gradlew bootRun
```

If not, install Gradle (8+) and run:

```bash
gradle bootRun
```

App runs on `http://localhost:8080`.

### H2 console

* URL: `http://localhost:8080/h2-console`
* JDBC URL: `jdbc:h2:mem:foodline`
* User: `sa`
* Password: (empty)

> Data is seeded on startup by `DataInitializer`.

---

## Seeded demo data

### User
| userId |
|---|
| `user101` |

### Outlets
| outletId | outletType | name |
|---|---|---|
| `store101` | `GROCERY_STORE` | FreshMart (Store 101) |
| `store102` | `GROCERY_STORE` | DailyGrocer (Store 102) |
| `rest201` | `RESTAURANT` | Noodle House (Rest 201) |

### Grocery items (inventory-driven)
| itemId | outletId | name | unitPrice |
|---|---|---|---|
| `g001` | `store101` | Apple | 1.20 |
| `g002` | `store101` | Milk | 3.50 |
| `g001_s2` | `store102` | Apple | 1.20 |
| `g002_s2` | `store102` | Milk | 3.50 |

### Food menu items (menu-driven)
| itemId | outletId | name | price |
|---|---|---|---|
| `f101` | `rest201` | Beef Noodles | 8.90 |
| `f102` | `rest201` | Fried Dumplings | 5.50 |

---

## API

### Add item to cart (unified)

```http
POST /cart/item
Content-Type: application/json
```

Request body:

```json
{
  "userId": "user101",
  "outletId": "store101",
  "catalogType": "GROCERY",
  "itemId": "g001",
  "quantity": 2,
  "overrideExistingCart": false
}
```

If the cart already contains items from another outlet/type and `overrideExistingCart=false`, you’ll get:

* **409 CONFLICT** with `overrideRequired=true`

To proceed, resend the same request with `overrideExistingCart=true`.

Response (200):

```json
{
  "cart": {
    "cartId": "cart_user101",
    "userId": "user101",
    "catalogType": "GROCERY",
    "outlet": { "outletId": "store101", "name": "FreshMart (Store 101)", "outletType": "GROCERY_STORE" },
    "items": [
      { "itemId": "g001", "name": "Apple", "unitPrice": 1.2, "quantity": 2, "catalogType": "GROCERY" }
    ]
  },
  "addedItem": { "itemId": "g001", "name": "Apple", "unitPrice": 1.2, "quantity": 2, "catalogType": "GROCERY" }
}
```

### View cart

```http
GET /cart/view?userId=user101
```

Returns a `CartResponse` (DTO).

### (Legacy) Add product to cart (grocery-only)

```http
POST /cart/product
Content-Type: application/json
```

```json
{
  "userId": "user101",
  "productId": "g001",
  "outletId": "store101"
}
```

> Kept for backwards compatibility.

---

## Orders

### Checkout

```http
POST /order/checkout?userId=user101
```

Creates an order from the cart, assigns fulfillment outlet, performs fulfillment, then clears the cart.

### Get order by id

```http
GET /order/{orderId}
```

Returns `OrderResponse`.

### List orders

```http
GET /order
GET /order?userId=user101
```

Returns `List<OrderSummaryResponse>` (lightweight list view).

---

## Useful Gradle commands

```bash
./gradlew test
./gradlew build
./gradlew bootRun
```

Or, without the wrapper:

```bash
gradle test
gradle build
gradle bootRun
```
