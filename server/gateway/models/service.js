var mongoose = require('mongoose');
var config = require('config');
var servicesDb = mongoose.createConnection(config.mongoose.SERVICES_DB_URL);

var serviceSchema = new mongoose.Schema ({
    name: String,
    url: String,
    endpoints: [ new mongoose.Schema({
        type: String,
        url: String
    }) ],
    authorizedRoles: [ String ]
}); 
var Service = servicesDb.model('Service', serviceSchema);

module.exports = Service;
