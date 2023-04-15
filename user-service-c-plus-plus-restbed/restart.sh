echo "recompile"
make clean
make
echo "starting"
DYLD_LIBRARY_PATH=libs/restbed/library ./server