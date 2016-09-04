module.exports = function (req, res, next) {
    var data = req.headers['authorization'];
    if (!data) {
       res.status(401).send();
       return;
    }

    data = data.split(" ");
    // The  Authorization: <type> <credentials> pattern was introduced by the W3C in HTTP 1.0.
    // Authorization : Bearer
    // format are most likely implementing OAuth 2.0 bearer tokens
    // https://www.httpwatch.com/httpgallery/authentication/
    if (data[0] !== "Bearer" || !data[1]) {
        res.status(401).send();
        return;
    }

    var token = data[1];
    try {
        var pa                logger.error(err);
            } else {
                callback({
                    user: user,
                    jwt: payload
                });
            }
        });
    } catch (err) {
        logger.error(err);
        callback(null);
    }
}