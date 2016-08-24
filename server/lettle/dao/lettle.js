var Lettle = require('../model/lettle');

module.exports =  {
    addLettle : function(req, res) {
        console.log("add lettle");
        var body = req.body;
        console.log('body : ' + JSON.stringify(body));
        var newLettle = Lettle(body);

        newLettle.save(function(e) {
        	if(e) throw err;
        	console.log('Lettle created');
            res.json({ message : 'ok'});
        });
    },
    getLettle : function(req,res, id) {
        console.log("get lettle");

        if(id) {    
            Lettle.find({senderId : id}, function(e, lettles) {
               res.json(lettles);
            });
        }
        else {
            Lettle.find({}, function(e, lettles) {
        	   res.json(lettles);
            });
        }
    },
    deleteLettle : function(req,res, id) {
        console.log("deleteLettle lettle");
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
};  
