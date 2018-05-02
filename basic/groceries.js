module.exports = function(context, cb) {
	//debug
	console.log(context);
	
	var cmd = context.data.cmd;
	if(cmd == "LIST"){
		//list out storage
		console.log("Listing items");
		context.storage.get(function (error, data) {
	        if (error) return cb(error);
	        if(!data){
	        	//no existing list
	        	console.log("No existing data");
	        	return cb(null, "No existing data");
	        }else{
	        	//simply return the list
	        	return cb(null, data);
	        }
		});
	}else if (cmd == "DEL"){
		//delete
		console.log("Deleting items");
		context.storage.get(function (error, data) {
			if (error) return cb(error);
			if(!data){
				//no existing list
				console.log("No existing data");
				return cb(null, "No existing data");
			}else{
				//loop through deletion list
				for(var delIndex = 0; delIndex < context.data.items.length; delIndex++){
					var delItem = context.data.items[delIndex];
					var matchIndex = data.indexOf(delItem);
					if(matchIndex == -1){
						console.log(newItem + " does not exist in list.");
					}else{
						console.log("Deleting item: " + delItem);
						data.splice(matchIndex, 1);
					}
				}
				
				//all deletions done, persist
		        context.storage.set(data, function (error) {
		        	if (error) return cb(error);
		        	cb(null, "Completed");
		        });
			}
		});
	}else{
		//assume put
		console.log("Putting items");
		context.storage.get(function (error, data) {
		        if (error) return cb(error);
		        //append to existing list
		        if(!data){
		        	//no existing list
		        	console.log("No existing data");
		        	data = context.data.items;
		        }else{
		        	//existing list, add non-duplicates
		        	for(var newIndex = 0; newIndex < context.data.items.length; newIndex++){
		        		var newItem = context.data.items[newIndex];
		        		var newEle = (data.indexOf(newItem) == -1);
		        		
		        		if(newEle){
		        			console.log("Adding new element: " + newItem);
		        			data.push(newItem);
		        		}else{
		        			console.log("Found duplicate: " + newItem);
		        		}
		        	}
		        	
		        }
		        //all setting/additions done, persist
		        context.storage.set(data, function (error) {
		        	if (error) return cb(error);
		        	cb(null, "Completed");
		        });
		 });
	}
}
