# Online Bookstore API

## Description
This is a RESTful API for a fictional online bookstore implemented using Spring Boot and Java.


## Requirements
- Java 17
- Maven
- H2 Database

## Getting Started

1.Build the project: mvn clean install
    if test file is throwing errors, then use  : mvn clean install -DskipTests

2.Run the application: mvn spring-boot:run



4.Access the Swagger documentation at: http://localhost:8080/swagger-ui/index.html

## Rate Limiting
- The API enforces rate limiting to allow only 60 requests per minute per client IP.


    
## API Endpoints

### User Management

	#### 1. Sign up
	- **URL:** `/auth/signup`
	- **Method:** `POST`
	- **Body:**
		```json
		 {
			"email":"danny",
			"password":"password",
			"fullName":"Daniel Ephraim"
		 }

		```
	- **Response:** `200 OK`
	- **Description:** Adds a new user.

	#### 1. Log in
	- **URL:** `/auth/login`
	- **Method:** `POST`
	- **Body:**
		```json
		 {
			"email":"danny",
			"password":"password"
		 }

		```
	- **Response:** `200 OK`
	- **Description:** Logs in new user and provides jwt token in return.


### Book Management

	#### 1. Add Book
	- **URL:** `/api/books`
	- **Method:** `POST`
	- **Body:**
		```json
		{
			"title": "Book Title",
			"author": "Author Name",
			"description": "Book Description",
			"genre": "Fiction"
		}
		```
	- **Response:** `200 OK`
	- **Description:** Adds a new book.

	#### 2. Get All Books
	- **URL:** `/api/books`
	- **Method:** `GET`
	- **Response:** `200 OK`
	- **Description:** Retrieves all books with associated reviews.

	#### 3. Get Book by ID
	- **URL:** `/api/books/{id}`
	- **Method:** `GET`
	- **Response:** `200 OK`
	- **Description:** Retrieves a book by its ID with associated reviews.

	#### 4. Update Book
	- **URL:** `/api/books/{id}`
	- **Method:** `PUT`
	- **Body:**
		```json
		{
			"title": "Updated Title",
			"author": "Updated Author",
			"description": "Updated Description",
			"genre": "Non-Fiction"
		}
		```
	- **Response:** `200 OK`
	- **Description:** Updates an existing book.

	#### 5. Delete Book by ID
	- **URL:** `/api/books/{id}`
	- **Method:** `DELETE`
	- **Response:** `204 No Content`
	- **Description:** Deletes a book by its ID along with associated reviews.

### Book Reviews and Ratings

	#### 1. Add Review
	- **URL:** `/api/reviews`
	- **Method:** `POST`
	- **Body:**
		```json
		{
			"rating": 5,
			"comment": "Great book!",
			"book": {
				"id": 1
			}
		}
		```
	- **Response:** `200 OK`
	- **Description:** Adds a new review. Users can only review a book once.

	#### 2. Update Review
	- **URL:** `/api/reviews/{id}`
	- **Method:** `PUT`
	- **Body:**
		```json
		{
			"rating": 4,
			"comment": "Updated comment",
			"book": {
				"id": 1
			}
		}
		```
	- **Response:** `200 OK`
	- **Description:** Updates an existing review.

	#### 3. Delete Review
	- **URL:** `/api/reviews/{id}`
	- **Method:** `DELETE`
	- **Response:** `204 No Content`
	- **Description:** Deletes a review by its ID.

	#### 4. Get All Reviews
	- **URL:** `/api/reviews`
	- **Method:** `GET`
	- **Response:** `200 OK`
	- **Description:** Retrieves all reviews.

	#### 5. Get Review by ID
	- **URL:** `/api/reviews/{id}`
	- **Method:** `GET`
	- **Response:** `200 OK`
	- **Description:** Retrieves a review by its ID.


