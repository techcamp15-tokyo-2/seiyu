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
		* ?email=string&pwd=string&uid=string&name=string&gender=string(0/1)&tag=
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
		timeSmap : int,
		gender : int,
		follow : int
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
		timeSmap : int,
		gender : int,
		follow : int
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
		timeSmap : int,
		gender : int,
		follow : int
		},
		]}

* /imageDetail
	* in
		* ?uid=string&seiyuId=string&page
	* out
		* {
		state : string,
		message : string,
		followed : int,
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
				    timeSmap : string,
					imageUrl : string,
					seiyuName : string,
					seiyuId : string
				},]
			},]
		}
		
* /user
	* in
		* ?uid=string&ouid=string
	* out
		* {
			state : string,
			message : string,
			name : string,
			email : string,
			seiyuList : [{
				seiyuId : string,
				seiyuName : string,
				followed : int
			},
			]
		}
