var formidable = require('formidable');
var config = require('config');
var util = require('util');
var AWS = require('aws-sdk');
var thunkify = require('thunkify');
var co = require('co');
var _ = require('lodash');

AWS.config.update({
    accessKeyId: config.storage.ki,
    secretAccessKey: config.storage.sk,
    region : 'ap-northeast-2',
});

var Pet = require('../models/pet/pet');
var Adop = require('../models/pet/adoption');
var logger = require('../logger');

//////////////////////////////////////////////////////////////////////////
// Pet
//////////////////////////////////////////////////////////////////////////
/**
 * 
 */
exports.getPet = function(req, res, next) {
    var id = req.params.id;
    if (id) {
        Pet.find({ senderId: id }, function (e, pets) {
            res.json(pets);
        });
    }
    else {
        Pet.find({}, function (e, pets) {
        	res.json(pets);
        });
    }
};

exports.addPet = function (req, res, next) {
    var body = req.body;
    //console.log('body : ' + JSON.stringify(body));
    var newPet = Pet(body);

    newPet.save(function(e) {
        if(e) throw err;
        res.json(body);
    });
};

exports.deletePet = function(req, res, next) {
    var id = req.params.id;
    if(!id) {
        return res.status(400).send();
    }

    Pet.remove( { _id : id }, function(err) {
        if(err) {
            return res.status(500).send({
                message : err
            });
        }
        else {
            return res.status(200).send({
                message : "ok"
            });
        }
    });
};


//////////////////////////////////////////////////////////////////////////
// Adoption
//////////////////////////////////////////////////////////////////////////

exports.getAdop = function(req, res, next) {
    var id = req.params.id;
    if (id) {
        Adop.findById(id , function (e, pets) {
            res.json(pets);
        });
    }
    else {
        Adop.find({}, function (e, pets) {
        	res.json(pets);
        });
    }
};

exports.addAdop = function (req, res, next) {
    var body = req.body;
    //console.log('body : ' + JSON.stringify(body));
    var newAdop = Adop(body);

    newAdop.save(function(e) {
        if(e) throw err;
        res.json(body);
    });
};

exports.deleteAdop = function(req, res, next) {
    var id = req.params.id;
    if(!id) {
        return res.status(400).send();
    }

    Adop.remove( { _id : id }, function(err) {
        if(err) {
            return res.status(500).send({
                message : err
            });
        }
        else {
            return res.status(200).send({
                message : "ok"
            });
        }
    });
};


function parseForm(req) {
    return  new Promise(function(resolve, reject){
        var form = new formidable.IncomingForm();
        form.parse(req, function(err, field, files) {
            if(err) {
                reject(err);
            }
            else {
                resolve(files);
            }
        });
    });
} 

function uploadToS3(file) {
    return  new Promise(function(resolve, reject) {
        var s3 = new AWS.S3();
        s3.upload(file, function(err, data) {
           if(err) {
                reject(err);
            }
            else {
                resolve(data);
            }     
        });
    });
}

exports.uploadPicture = function(req, res, next) {
    co(function*(){
        var images = yield parseForm(req);
        var params = _.transform(images, function(result, value, key) {
            result.push({
                Bucket : config.storage.bn,
                Key : value.name,
                ACL : 'public-read',
                Body : require('fs').createReadStream(value.path),
                //ContentType: files.mimetype
            });
        }, []);

        var results = yield _.transform(params , function(result, param) {
            result.push(uploadToS3(param))
        }, []);

        var locations = _.map(results, "Location");
        if(locations.length > 0) {
            var adop = yield Adop.findById(req.params.id).exec();
            if(adop) {
                for(var url of locations) {
                    if(adop.picture.length >=3 ) {
                        adop.picture.shift();
                    }
                    adop.picture.push(url);
                }
                adop.save();
            }
        }
        res.status(200).send({
            message : "ok",
            data : locations
        });
    })
    .catch(function(e) {
        console(e.stack);
        res.status(500).send({
            message : e
        });;
    });

   
    
};


