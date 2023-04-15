#include <memory>
#include <cstdlib>
#include "../libs/restbed/include/restbed"
#include "../libs/json.hpp"
#include "server_handlers.h"

using json = nlohmann::json;
using namespace std;
using namespace restbed;

class User
{
public:
    string username;
    long id;
};

int main(const int, const char **)
{
    auto resource_with_id = make_shared<Resource>();
    resource_with_id->set_path("/api/users/{id: [0-9]+}");
    resource_with_id->set_method_handler("GET", get_user_handler);
    resource_with_id->set_method_handler("PUT", put_user_handler);
    resource_with_id->set_method_handler("DELETE", delete_user_handler);
    resource_with_id->set_default_header("Content-Type", "application/json");

    auto resource_wo_id = make_shared<Resource>();
    resource_wo_id->set_path("/api/users");
    resource_wo_id->set_method_handler("GET", get_users_handler);
    resource_wo_id->set_method_handler("POST", post_method_handler);
    resource_wo_id->set_default_header("Content-Type", "application/json");

    auto settings = make_shared<Settings>();
    settings->set_port(8080);
    settings->set_default_header("Connection", "close");

    Service service;
    service.publish(resource_with_id);
    service.publish(resource_wo_id);
    service.start(settings);

    return EXIT_SUCCESS;
}