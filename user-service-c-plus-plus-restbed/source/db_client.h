#include <vector>
#include "user.h"

using namespace std;

std::vector<User> get_users();
User *get_user_by_id(string id);
User *get_user_by_username(string username);
User *update_user_by_id(string id, string username);
User *create_user(string username);
User *delete_user_by_id(string id);
