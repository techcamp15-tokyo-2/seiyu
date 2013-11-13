###Instance
	
* user
	* oid : string
	* name : string
	* email : string
	* pwd :string
	* gender : int
	* nick : string
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
	* latestCrawlerTime : string
###Interface
* /login
	* in 
		* ?email=string&pwd=string&uid=string
	* out
		* {
		state : string,
		message : string,
		email : string,
		name : string,
		gender : string
		tag : string (,分割)
			}

* /register
	* in
		* ?email=string&pwd=string&uid=string&name=string&gender=string(0/1)
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
		* {
		state : string,
		message : string,
		url : string
		}

* /latestFeed
	* in
		* ?uid=string&page=0,1,2,3,4
	* out
		* {
		state : string,
		message : string,
		imageList : [
		{imageUrl : string,
		seiyuName : string,
		seiyuId : string,
		timeSmap : int
		},
		]}

* /favourite
	* in
		* ?uid = string&page=
	* out
		* {
		state : string,
		message : string,
		imageList : [
		{imageUrl : string,
		seiyuName : string,
		seiyuId : string,
		timeSmap : int
		},
		]}

* /search
	* in
		* ?uid=string&keyword=string&page=
	* out
		* {
		state : string,
		message : string,
		imageList : [
		{imageUrl : string,
		seiyuName : string,
		seiyuId : string,
		timeSmap : int
		},
		]}

* /imageDetail
	* in
		* ?uid=string&seiyuId=string&page
	* out
		* {
		state : string,
		message : string,
		imageList : [{
			imageUrl : string,
			blogUrl : string
		},]
		}

* /blogDetail
	* in
		* ?uid=string&seiyuId=string&page
	* out
		* {
		state : string,
		message : string,

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
