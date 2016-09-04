var _ = require('underscore');
var url = require('url');

/*
 * Authentication validation using JWT. Strategy: find existing user.
 */
function validateAuth(data, callback) {
    if(!data) {  
        callback(null);
        return;
    }
    
    data = data.split(" ");
    // The  Authorization: <type> <credentials> pattern was introduced by the W3C in HTTP 1.0.
    // Authorization : Bearer
    // format are most likely implementing OAuth 2.0 bearer tokens
    // https://www.httpwatch.com/httpgallery/authentication/
    if(data[0] !== "Bearer" || !data[1]) {
        callback(null);
        return;
    }
    
    var token = data[1];    
    try {
        var payload = jwt.verify(token, secretKey);
        // Custom validation logic, in this case we just check that the 
        // user exists
        User.findOne({ id: payload.sub }, function(err, user) {
            if(err) {
                logger.error(err);
            } else {
                callback({
                    user: user,
                    jwt: payload 
                });
            }
        });                
    } catch(err) {
        logger.error(err);
        callback(null);
    }
}

function roleCheck(user, service) {
    //_.intersection([1, 2, 3], [101, 2, 1, 10], [2, 1]);
    // => [1, 2]
    var intersection = _.intersection(user.roles, service.authorizedRoles);
    return intersection.length === service.authorizedRoles.length;
}

function dispatch(req, res, next) {
    // Authentication
    /*
    var authHeader = req.headers["authorization"];
    validateAuth(authHeader, function(authPayload) {
        if(!authPayload) {
            res.status(401).send();
            return;
        }
        
        // We keep the authentication payload to pass it to 
        // microservices decoded.
        req.context = {
            authPayload: authPayload
        };
      
        serviceDispatch(req, res, next);        
    });
    */
    //serviceDispatch(req, res, next);   
   var req = http.request(req, function(res) {
        var resData = "";
        res.on('data', function (chunk) {
            resData += chunk;
        });
        res.on('end', function() {
            try {
                var json = JSON.parse(resData);
                deferred.resolve(json);
            } catch(err) {
                deferred.reject({
                    req: oldReq, 
                    endpoint: endpoint, 
                    message: 'Invalid data format: ' + err.toString()
                });
            }
        });
    });
}



/* 
 * Parses the request and dispatches multiple concurrent requests to each
 * internal endpoint. Results are aggregated and returned.
 */
function serviceDispatch(req, res, next) {
    var parsedUrl = url.parse(req.url);
    
    Service.findOne({ url: parsedUrl.pathname }, function(err, service) {
        if(err) {
            logger.error(err);
            res.status(500).send();
            return;
        }
    
        var authorized = roleCheck(req.context.authPayload.user, service);
        if(!authorized) {
            res.status(401).send();
            return;
        }       
        
        // Fanout all requests to all related endpoints. 
        // Results are aggregated (more complex strategies are possible).
        var promises = [];
        service.endpoints.forEach(function(endpoint) {   
            logger.debug(sprintf('Dispatching request from public endpoint ' + 
                '%s to internal endpoint %s (%s)', 
                req.url, endpoint.url, endpoint.type));
                         
            switch(endpoint.type) {
                case 'http-get':
                case 'http-post':
                    promises.push(httpPromise(req, endpoint.url, 
                        endpoint.type === 'http-get'));
                    break;
                case 'amqp':
                    promises.push(amqpPromise(req, endpoint.url));
                    break;
                default:
                    logger.error('Unknown endpoint type: ' + endpoint.type);
            }            
        });
        
        //Aggregation strategy for multiple endpoints.
        Q.allSettled(promises).then(function(results) {
            var responseData = {};
        
            results.forEach(function(result) {
                if(result.state === 'fulfilled') {
                    responseData = _.extend(responseData, result.value);
                } else {
                    logger.error(result.reason.message);
                }
            });
            
            res.setHeader('Content-Type', 'application/json');
            res.end(JSON.stringify(responseData));
        });
    }, 'services');
}

module.exports = dispatch;