
quartz {

//	if (System.env.JOB_ACTIVE?.equals("true")){
		autoStartup = true
//	  } else {
//		autoStartup = false
//	  }

    jdbcStore = false
    waitForJobsToCompleteOnShutdown = false
}

environments {
    test {
        quartz {
            autoStartup = false
        }
    }
	production {
        quartz {
            autoStartup = true
        }
    }
	
}

