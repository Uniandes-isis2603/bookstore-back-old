/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package co.edu.uniandes.dse.bookstore.exceptions;

public final class ErrorMessage {
	public static final String BOOK_NOT_FOUND = "The book with the given id was not found";
	public static final String REVIEW_NOT_FOUND = "The review with the given id was not found";
	public static final String EDITORIAL_NOT_FOUND = "The editorial with the given id was not found";
	public static final String PRIZE_NOT_FOUND = "The prize with the given id was not found";
	public static final String AUTHOR_NOT_FOUND = "The author with the given id was not found";
	public static final String ORGANIZATION_NOT_FOUND = "The organization with the given id was not found";

	private ErrorMessage() {
		throw new IllegalStateException("Utility class");
	}
}