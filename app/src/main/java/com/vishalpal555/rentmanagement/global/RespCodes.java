package com.vishalpal555.rentmanagement.global;

public class RespCodes {
    public static final int HTTP_OK = 200; //OK: The request was successful, and the server has returned the requested data.
    public static final int HTTP_CREATED = 201; //Created: The request has been fulfilled, resulting in the creation of a new resource.
    public static final int HTTP_ACCEPTED = 202; //Accepted: The request has been accepted for processing but has not been completed yet.
    public static final int HTTP_NO_CONTENT = 204; //No Content: The request was successful, but there is no response body.
    public static final int HTTP_MOVED_PERMANENTLY = 301; //Moved Permanently: The requested resource has been permanently moved to a new URL.
    public static final int HTTP_FOUND = 302; //Found: The requested resource has been temporarily moved to a different URL.
    public static final int HTTP_SEE_OTHER = 303; //See Other: The response to the request can be found at a different URL.
    public static final int HTTP_BAD_REQUEST = 400; //Request: The server couldn't understand the request due to invalid syntax or missing parameters.
    public static final int HTTP_UNAUTHORIZED = 401; //Unauthorized: Authentication is required, and the credentials provided were either missing or invalid.
    public static final int HTTP_FORBIDDEN = 403; //Forbidden: The server understood the request but refuses to fulfill it.
    public static final int HTTP_NOT_FOUND = 404; //Not Found: The requested resource could not be found on the server.
    public static final int HTTP_METHOD_NOT_ALLOWED = 405; //Method Not Allowed: The HTTP method used is not allowed for the requested resource.
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500; //Internal Server Error: A generic error message indicating that the server encountered an unexpected condition.
    public static final int HTTP_NOT_IMPLEMENTED = 501; //Not Implemented: The server does not support the functionality required to fulfill the request.
    public static final int HTTP_SERVICE_UNAVAILABLE = 503; //Service Unavailable: The server is temporarily unable to handle the request.
}
