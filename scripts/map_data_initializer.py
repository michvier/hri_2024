import json
import os
import time
from datetime import datetime
from pathlib import Path

import subprocess
import sys
import pymongo

subprocess.check_call([sys.executable, "-m", "pip", "install", 'pymongo'])


DATABASE = 'AQOAP'
COLLECTION = 'AQOAP_MAPS'



def get_maps(path: str, maps: list = []) -> list:
    for child in Path(path).iterdir():
        file_name = f"{path}/{child.name}"
        if child.is_dir():
            get_maps(file_name)
        elif child.is_file():
            maps.append(file_name)
    return maps


def init_db():
    db = mydb.testDB
    mydb.create_collection(name='ts_test1', timeseries={ 'timeField': 'timestamp', 'metaField': 'data', 'granularity': 'hours' })



if __name__ == '__main__':
    global mydb
    myclient = pymongo.MongoClient('mongodb://localhost:27777')
    mydb = myclient[DATABASE]
    col= mydb.get_collection(COLLECTION)

    maps = get_maps("./map_data")


    for map_path in maps:
        with open(map_path) as mapjson:
            data = json.load(mapjson)
            data['mapName'] = mapjson.name.split("/")[-1].split(".")[0].strip()
            col.insert_one(data)
