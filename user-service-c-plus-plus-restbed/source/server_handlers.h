#include <memory>
#include <cstdlib>
#include "../libs/restbed/include/restbed"
#include "../libs/json.hpp"
#include "../libs/postgre-client/include/libpq-fe.h"

using json = nlohmann::json;
using namespace std;
using namespace restbed;

void get_users_handler(const shared_ptr<Session> &session);
void get_user_handler(const shared_ptr<Session> &session);
void put_user_handler(const shared_ptr<Session> &session);
void delete_user_handler(const shared_ptr<Session> &session);
void post_method_handler(const shared_ptr<Session> &session);