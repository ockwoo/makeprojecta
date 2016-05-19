var express = require('express');
var router = express.Router();

router.get('/', function(req, res){
    res.json({message: 'Welcome to API world'});
});

router.post('/feed', function(req, res){
    res.json({message: 'feed'});
});

module.exports = router;