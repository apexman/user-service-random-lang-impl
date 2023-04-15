#include <memory>
#include <cstdlib>
#include <vector>
#include "../libs/restbed/include/restbed"
#include "../libs/json.hpp"
#include "../libs/postgre-client/include/libpq-fe.h"
#include "user.h"
#include "db_client.h"

using json = nlohmann::json;
using namespace std;
using namespace restbed;

void get_users_handler(const shared_ptr<Session> &session)
{
    const auto request = session->get_request();

    json users_json = json::array();
    std::vector<User> users = get_users();
    for (const auto &user : users)
    {
        json user_json = {{"id", user.id}, {"username", user.username}};
        users_json.push_back(user_json);
    }

    // Set response body as JSON string
    session->close(OK, users_json.dump());
}
using namespace restbed;

void get_user_handler(const shared_ptr<Session> &session)
{
    const auto request = session->get_request();
    const auto id = request->get_path_parameter("id");

    User *user_ptr = get_user_by_id(id);
    if (user_ptr == nullptr)
    {
        session->close(NOT_FOUND);
        return;
    }
    User user = *user_ptr;

    // Serialize user object to JSON
    json user_json = {
        {"id", user.id},
        {"username", user.username}};

    // Set response body as JSON string
    session->close(OK, user_json.dump());
}

void post_method_handler(const shared_ptr<Session> &session)
{
    const auto request = session->get_request();
    int content_length = request->get_header("Content-Length", 0);
    session->fetch(
        content_length,
        [](const shared_ptr<Session> session, const Bytes &body) {
        });

    const json body = json::parse(request->get_body());
    if (body.find("username") == body.end())
    {
        session->close(BAD_REQUEST);
        return;
    }

    const string username = body["username"];
    fprintf(stderr, "body: %s\n", body.dump().c_str());
    fprintf(stderr, "got username: %s\n", username.c_str());

    if (get_user_by_username(username) != nullptr)
    {
        session->close(CONFLICT);
        return;
    }

    User *user_ptr = create_user(username);
    if (user_ptr == nullptr)
    {
        session->close(NOT_FOUND);
        return;
    }
    User user = *user_ptr;

    // Serialize user object to JSON
    json user_json = {
        {"id", user.id},
        {"username", user.username}};

    // Set response body as JSON string
    session->close(OK, user_json.dump());
}

void put_user_handler(const shared_ptr<Session> &session)
{
    const auto request = session->get_request();
    const auto id = request->get_path_parameter("id");

    int content_length = request->get_header("Content-Length", 0);
    session->fetch(
        content_length,
        [](const shared_ptr<Session> session, const Bytes &body) {
        });

    const json body = json::parse(request->get_body());
    if (body.find("username") == body.end())
    {
        User *user_ptr = get_user_by_id(id);
        if (user_ptr == nullptr)
        {
            session->close(NOT_FOUND);
            return;
        }
        User user = *user_ptr;

        // Serialize user object to JSON
        json user_json = {
            {"id", user.id},
            {"username", user.username}};

        // Set response body as JSON string
        session->close(OK, user_json.dump());
        return;
    }

    const string username = body["username"];
    fprintf(stderr, "body: %s\n", body.dump().c_str());
    fprintf(stderr, "got username: %s\n", username.c_str());

    if (get_user_by_username(username) != nullptr)
    {
        session->close(CONFLICT);
        return;
    }

    User *user_ptr = update_user_by_id(id, username);
    if (user_ptr == nullptr)
    {
        session->close(NOT_FOUND);
        return;
    }
    User user = *user_ptr;

    // Serialize user object to JSON
    json user_json = {
        {"id", user.id},
        {"username", user.username}};

    // Set response body as JSON string
    session->close(OK, user_json.dump());
}

void delete_user_handler(const shared_ptr<Session> &session)
{
    const auto request = session->get_request();
    const auto id = request->get_path_parameter("id");

    delete_user_by_id(id);

    // const auto response = make_shared<Response>(NO_CONTENT);
    session->close(OK);
}