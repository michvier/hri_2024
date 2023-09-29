# Explanation Dashboard

Installation and Startup:

0) Prerequisites: Python3, docker, and docker-compose must be installed (https://docs.docker.com/compose)

1) Clone/download this repository
2) Build the docker image for the Dashboard: `docker build --tag aqoap-dashboard .  --platform linux/x86_64`
3) Run docker-compose to start the application: `docker-compose -f docker_compose.yaml -p aqoap up`
4) Add sample maps to the database: run the python script in the folder scrips: <br>
   `cd scripts`  <br> `python map_data_initializer.py`

5) Open Explanation Dashboard Demo at [https://localhost:8888](http://localhost:8888/)
