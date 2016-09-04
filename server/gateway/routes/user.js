var express = require('express');
var router = express.Router();

var userCtrl = require('../controller/user');

/* GET users listing. */
router.get('/', userCtrl.getUser);

router.post('/login', userCtrl.login);
router.post('/register', userCtrl.register);

module.exports = router;
