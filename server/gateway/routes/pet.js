var express = require('express');
var router = express.Router();
var petCtrl = require('../controller/pet');

router.get('/pet', petCtrl.getPet);
router.get('/pet/:id', petCtrl.getPet);
router.post('/pet', petCtrl.addPet);
router.delete('/pet/:id', petCtrl.deletePet);

router.get('/adoption', petCtrl.getAdop);
router.get('/adoption/:id', petCtrl.getAdop);
router.post('/adoption', petCtrl.addAdop);
router.delete('/adoption/:id', petCtrl.deleteAdop);

router.post('/adoption/:id/picture', petCtrl.uploadPicture);

module.exports = router;