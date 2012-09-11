
//checkbox settings
ESGF.setting.replicas = 'false';
ESGF.setting.versionsLatest = 'true';
ESGF.setting.distributed = 'true';


if(ESGF.localStorage.toKeyArr('esgf_queryString') != null) {
		
		var latest = ESGF.localStorage.get('esgf_queryString','latest:true');
		if(latest == undefined) {
			ESGF.setting.versionsLatest = null;
		}
		
		var replica = ESGF.localStorage.get('esgf_queryString','replica:false');
		if(replica == undefined) {
			ESGF.setting.replicas = null;
		} 
		
		
		
		var distrib = ESGF.localStorage.get('esgf_queryString','distrib');
		
		if(distrib == 'distrib=false') {
			ESGF.setting.distributed = 'false';
		} 
		
		
} 