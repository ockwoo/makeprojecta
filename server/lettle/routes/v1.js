var express = require('express');
var router = express.Router();
var lettleDb = require('../dao/lettle');

router.get('/', function(req, res){
    res.json({message: 'Welcome to API world'});
});

router.get('/feed', function(req, res){
    lettleDb.getLettle(req, res);
    res.json({message: 'feed'});
});

router.post('/feed', function(req, res){
    lettleDb.addLettle(req, res);
    res.json({message: 'feed'});
});

module.exports = router;