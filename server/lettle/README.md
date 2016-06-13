# Lettle

## Install node.js

## Install Mongodb

* mongod 수행

## Download source from github

	$ git clone git@github.com:ockwoo/makeprojecta.git
	$ cd makeprojecta/server/lettle/
	$ npm install
	$ cd bin

## Search for lettle from Mongodb

	D:\ock\program\mongodb-win32-x86_64-3.0.7\bin>mongo 127.0.0.1:27017/test
	MongoDB shell version: 3.0.7
	connecting to: test
	test> use test
	switched to db test
	test> db.getCollectionNames()
	[ "lettles", "system.indexes" ]
	test> db.find()
	test> db.lettles.find()
	{ "_id" : ObjectId("5750546e29e8aa8818166e18"), "updated_at" : ISODate("2016-06-02T15:44:46.493Z"), "receiversId" : [ ], "__v" : 0 }
	{ "_id" : ObjectId("57505677965e46c4162b256f"), "updated_at" : ISODate("2016-06-02T15:53:27.862Z"), "receiversId" : [ ], "__v" : 0 }
	{ "_id" : ObjectId("575b774d626824600c557dfa"), "updated_at" : ISODate("2016-06-11T02:28:29.819Z"), "receiversId" : [ ], "__v" : 0 }


## Install

Install the command line tool

    $ npm install
    
Install foreman

    $ npm install -g foreman

## Startup Server

    $ nf start 
