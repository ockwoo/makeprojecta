var Lettle = require('../model/lettle');

module.exports =  {
    addLettle : function(req, res) {
        console.log("add lettle");
        var body = req.body;
        var newLettle = Lettle(body);

        newLettle.save(function(e) {
        	if(e) throw err;
        	console.log('Lettle created');
            res.json({ message : 'ok'});
        });
    },
    getLettle : function(req,res) {
        console.log("get lettle");
        Lettle.find({}, function(e, lettles) {
        	res.json(lettles);
        });
    }
};  
