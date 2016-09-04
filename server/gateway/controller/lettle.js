var Lettle = require('../models/lettle/lettle');

exports.get = function (req, res, next) {
    var id = req.params.id;
    if (id) {
        Lettle.find({ senderId: id }, function (e, lettles) {
            res.json(lettles);
        });
    }
    else {
        Lettle.find({}, function (e, lettles) {
        	res.json(lettles);
        });
    }
};

exports.add = function (req, res, next) {
    var body = req.body;
    //console.log('body : ' + JSON.stringify(body));
    var newLettle = Lettle(body);

    newLettle.save(function(e) {
        if(e) throw err;
        console.log('Lettle created');
        res.json({ message : 'ok'});
    });
};

exports.delete = function(req, res, next) {
    var id = req.params.id;
    if(!id) {
        return res.status(400).send();
    }

    Lettle.remove( { _id : id }, function(err) {
        if(err) {
            return res.status(500).send();
        }
        else {
            return res.status(200).send();
        }
    });
}