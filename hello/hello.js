module.exports = function(context, cb) {
//	cb(null, { i_am: 'done '});
	console.log(context);
	console.log(context.body);
	console.log(context.body.name);
	cb(null, { hello: context.body.name || 'Anonymous' });
}
