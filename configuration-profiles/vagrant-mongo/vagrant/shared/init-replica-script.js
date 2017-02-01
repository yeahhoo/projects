db = (new Mongo('192.168.12.102:40000')).getDB('test')

config = {
  	"_id" : "my-mongo-set",
  	"members" : [
  		{
  			"_id" : 0,
  			"host" : "192.168.12.100:40000"
  		},
  		{
  			"_id" : 1,
  			"host" : "192.168.12.101:40000"
  		},
  		{
  			"_id" : 2,
  			"host" : "192.168.12.102:40000"
  		}
  	]
  }

rs.initiate(config)