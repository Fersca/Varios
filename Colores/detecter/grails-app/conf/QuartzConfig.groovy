
quartz {

//	if (System.env.JOB_ACTIVE?.equals("true")){
		autoStartup = false
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
            autoStartup = false
        }
    }
	
}

