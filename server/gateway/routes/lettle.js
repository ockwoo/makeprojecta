var express = require('express');
var router = express.Router();
var lettleCtrl = require('../controller/lettle');

router.get('/', lettleCtrl.get);
router.get('/:id', lettleCtrl.get);

router.post('/', lettleCtrl.add);
router.delete('/:id', lettleCtrl.delete);

module.exports = router;