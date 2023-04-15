#include <vector>
#include "../libs/postgre-client/include/libpq-fe.h"
#include "user.h"

using namespace std;

PGconn *get_connection()
{
    PGconn *conn = PQconnectdb("dbname=testing_user_service user=postgres password=postgres hostaddr=127.0.0.1 port=5432");
    if (PQstatus(conn) != CONNECTION_OK)
    {
        fprintf(stderr, "%s\n", "Connection to database failed:\n");
        fprintf(stderr, "%s\n", PQerrorMessage(conn));
        PQfinish(conn);
        return NULL;
    }
    return conn;
}

std::vector<User> get_users()
{
    std::vector<User> users;

    // Connect to the database
    PGconn *conn = get_connection();
    if (conn == nullptr)
    {
        return users;
    }

    
    // Execute the query
    PGresult *res = PQexec(conn, "SELECT id, username FROM user_service_schema.users");
    if (PQresultStatus(res) != PGRES_TUPLES_OK) {
        fprintf(stderr, "Query failed: %s", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        return users;
    }

    // Extract the users from the result
    int num_rows = PQntuples(res);
    for (int i = 0; i < num_rows; i++) {
        User user;
        user.id = std::stoi(PQgetvalue(res, i, 0));
        user.username = PQgetvalue(res, i, 1);
        users.push_back(user);
    }

    // Clean up
    PQclear(res);
    PQfinish(conn);

    return users;
}

User *get_user_by_id(string id)
{
    // Connect to the database
    PGconn *conn = get_connection();
    if (conn == nullptr)
    {
        return NULL;
    }

    // Prepare the query
    const char *paramValues[1];
    paramValues[0] = id.c_str(); // ID of the user to select
    PGresult *res =
        PQexecParams(conn,
                     "SELECT * FROM user_service_schema.users WHERE id = $1",
                     1,    /* one param */
                     NULL, /* let the backend deduce param type */
                     paramValues,
                     NULL, /* don't need param lengths since text */
                     NULL, /* default to all text params */
                     0);   /* ask for non-binary results */
    if (PQresultStatus(res) != PGRES_TUPLES_OK)
    {
        fprintf(stderr, "%s\n", "Query failed:\n");
        fprintf(stderr, "%s\n", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    // Extract the user from the result
    int num_rows = PQntuples(res);
    if (num_rows == 0)
    {
        fprintf(stderr, "%s\n", "User not found");
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    User user;
    user.id = std::stoi(PQgetvalue(res, 0, 0));
    user.username = PQgetvalue(res, 0, 1);

    // Clean up
    PQclear(res);
    PQfinish(conn);

    return &user;
}

User *get_user_by_username(string username)
{
    // Connect to the database
    PGconn *conn = get_connection();
    if (conn == nullptr)
    {
        return NULL;
    }

    // Prepare the query
    const char *paramValues[1];
    paramValues[0] = username.c_str(); // ID of the user to select
    PGresult *res =
        PQexecParams(conn,
                     "SELECT * FROM user_service_schema.users WHERE username = $1",
                     1,    /* one param */
                     NULL, /* let the backend deduce param type */
                     paramValues,
                     NULL, /* don't need param lengths since text */
                     NULL, /* default to all text params */
                     0);   /* ask for non-binary results */
    if (PQresultStatus(res) != PGRES_TUPLES_OK)
    {
        fprintf(stderr, "%s\n", "Query failed:\n");
        fprintf(stderr, "%s\n", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    // Extract the user from the result
    int num_rows = PQntuples(res);
    if (num_rows == 0)
    {
        fprintf(stderr, "%s\n", "User not found");
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    User user;
    user.id = std::stoi(PQgetvalue(res, 0, 0));
    user.username = PQgetvalue(res, 0, 1);

    // Clean up
    PQclear(res);
    PQfinish(conn);

    return &user;
}

User *update_user_by_id(string id, string username)
{
    // Connect to the database
    PGconn *conn = get_connection();
    if (conn == nullptr)
    {
        return NULL;
    }

    // Prepare the query
    const char *paramValues[2];
    paramValues[0] = username.c_str();
    paramValues[1] = id.c_str();
    PGresult *res =
        PQexecParams(conn,
                     "UPDATE user_service_schema.users SET username = $1 WHERE id = $2",
                     2,
                     NULL,
                     paramValues,
                     NULL,
                     NULL,
                     0);
    if (PQresultStatus(res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "%s\n", "Query failed:");
        fprintf(stderr, "%s\n", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    // Clean up
    PQclear(res);
    PQfinish(conn);

    return get_user_by_id(id);
}

User *create_user(string username)
{
    // Connect to the database
    PGconn *conn = get_connection();
    if (conn == nullptr)
    {
        return NULL;
    }

    // Prepare the query
    const char *paramValues[1];
    paramValues[0] = username.c_str();
    PGresult *res =
        PQexecParams(conn,
                     "INSERT INTO user_service_schema.users (username, created_at, last_modified_at) VALUES($1, now(), now()) RETURNING id;",
                     1,
                     NULL,
                     paramValues,
                     NULL,
                     NULL,
                     0);
    if (PQresultStatus(res) != PGRES_TUPLES_OK)
    {
        fprintf(stderr, "%s\n", "Query failed:");
        fprintf(stderr, "%s\n", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    // Extract the user from the result
    int num_rows = PQntuples(res);
    if (num_rows == 0)
    {
        fprintf(stderr, "%s\n", "User not found");
        PQclear(res);
        PQfinish(conn);
        return NULL;
    }

    string id = PQgetvalue(res, 0, 0);

    // Clean up
    PQclear(res);
    PQfinish(conn);

    return get_user_by_id(id);
}

void delete_user_by_id(string id)
{
    // Connect to the database
    PGconn *conn = get_connection();
    if (conn == nullptr)
    {
        return;
    }

    // Prepare the query
    const char *paramValues[1];
    paramValues[0] = id.c_str();
    PGresult *res =
        PQexecParams(conn,
                     "DELETE FROM user_service_schema.users WHERE id = $1;",
                     1,
                     NULL,
                     paramValues,
                     NULL,
                     NULL,
                     0);
    if (PQresultStatus(res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "%s\n", "Query failed:");
        fprintf(stderr, "%s\n", PQerrorMessage(conn));
        PQclear(res);
        PQfinish(conn);
        return;
    }

    // Clean up
    PQclear(res);
    PQfinish(conn);

    return;
}