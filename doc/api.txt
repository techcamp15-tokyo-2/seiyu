###Instance
	
* user
	* oid : string
	* name : string
	* email : string
	* pwd :string
	* gender : int
	* tag : [string,]
	* followed : [string,]
* seiyu
	* oid: string
	* name : string
	* blogPrefxi : string
* image
	* oid : string
	* timeSmap : int
	* imageUrl : string
	* blogUrl : string
	* blogName : string

###Interface
* /login
	* in 
		* ?email=string&pwd=string&uid=string
	* out
		* {
		state : string,
		message : string,
		email : string,
		name : string
			}

* /register
	* in
		* ?email=string&pwd=string&uid=string
	* out
		* {
		state : string,
		message : string,
		email : string,
		name : string
			}

* /findPwd
	* in
		* ?uid=string&email=string
	* out
		* None

* /latestFeed
	* in
		* ?uid=string
	* out
		* {
		state : string,
		message : string,
		imageList : [
		{imageUrl : string,
		seiyuName : string,
		seiyuId : string
		},
		]}

* /favourite
	* in
		* ?uid = string
	* out
		* {
		state : string,
		message : string,
		imageList : [
		{imageUrl : string,
		seiyuName : string,
		seiyuId : string
		},
		]}

* /search
	* in
		* ?uid=string&keyword=string
	* out
		* {
		state : string,
		message : string,
		imageList : [
		{imageUrl : string,
		seiyuName : string,
		seiyuId : string
		},
		]}

* /detail
	* in
		* ?uid=string&seiyuId=string
	* out
		* {
		state : string,
		message : string,
		imageList : [{
			imageUrl : string,
			blogUrl : string
		},],
		blogList : [{
			blogName : string,
			blogUrl : string,
			timeSmap : int
		},]
		}

* /action
	* in
		* ?uid=string&seiyuId=string&followed=int
	* out
		* {
			state : string,
			message : string
		}
*/recommend
	* in
		* ?uid=string
	* out
		* {
			state : string,
			message : string,
			infoList : [{
				userId : string,
				userName : string,
				imageList : [{
					imageUrl : string,
					seiyuName : string,
					seiyuId : string
				},]
			},]
		}
		
* /editInfo
	* in
		* ?uid=string&tags=(string = string,string,string)string&email=string
	* out
		* {
			state : string,
			message : string
		}