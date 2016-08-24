// app.js
var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');

var logger = require('./utils/logger');

var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var routes = require('./routes/index');
var users = require('./routes/users');
var v1 = require('./routes/v1');

var config = require('config'); // get our config file
var mongoose = require('mongoose'); // mongo connect

var app = express();


//=============================================
// config
//=============================================
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(require('morgan')('dev', {
  'stream' : logger.stream
}));

mongoose.connect(config.mongodb);
mongoose.connection.on('error', function () {
    console.log('Mongoose connection error');
});
mongoose.connection.once('open', function callback() {
    console.log("Mongoose connected to the database");
});

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', routes);
app.use('/users', users);
app.use('/v1', v1);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});

module.exports = app;
