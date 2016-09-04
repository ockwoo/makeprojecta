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
var config = require('config');
var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var db = mongoose.createConnection(config.mongoose.LETTLE_DB_URL);
/*
var schema = new Schema({
  name:    String,
  binary:  Buffer,
  living:  Boolean,
  updated: { type: Date, default: Date.now },
  age:     { type: Number, min: 18, max: 65 },
  mixed:   Schema.Types.Mixed,
  _someId: Schema.Types.ObjectId,
  array:      [],
  ofString:   [String],
  ofNumber:   [Number],
  ofDates:    [Date],
  ofBuffer:   [Buffer],
  ofBoolean:  [Boolean],
  ofMixed:    [Schema.Types.Mixed],
  ofObjectId: [Schema.Types.ObjectId],
  nested: {
    stuff: { type: String, lowercase: true, trim: true }
  }
})
*/
var lettleSchema = new Schema({
	senderId : String,
	receiversId : [String],
	contents : {
		title : String,
		msg : String
	},
	event : {
		latitude : Number,
		longitude : Number,
		timestamp : Date
	},
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

var Lettle = db.model('Lettle', lettleSchema);
module.exports = Lettle;