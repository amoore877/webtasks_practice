//doesn't seem to work; "express" not found
var app = new (require('express'))();
app.get('/', (req, res) => res.send('Hello World'));
module.exports = app;