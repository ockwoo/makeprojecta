/*
Letter Schema (MONGO)
-. ID
-. SENDER ID
-. RECEIVERS ID
-. CONTENTS : string
-. EVENT  
-. CRTE TM
-. MODY TM
-. CREATOR 
-. MODIFIER
*/
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var lettleSchema = new Schema({
	senderId : String,
	receiversId : Array,
	contents : String,
	event : String ,
	created_at : Date,
	updated_at : Date,
	creator : String,
	modifier : String
});

/**
	Run a function Before Saving
	on every save, add the date
	This is also a great place to hash passwords 
*/
lettleSchema.pre('save', function(next) {
	var currentDate = new Date();
	this.updated_at = currentDate;
	if(!this.create_at) {
		this.create_at = currentDate;
	}
	next();
});

var Lettle = mongoose.model('Lettle', lettleSchema);
module.exports = Lettle;