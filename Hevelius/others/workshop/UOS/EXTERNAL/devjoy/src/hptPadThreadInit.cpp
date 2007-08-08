/*
 "@(#) $Id: "
*/

// create and start workerThread


    DO_running = false;
    getComponent()->getThreadManager()->create("workerThread",
                                      (void *)Pad::workerThread,
                                      (void *)this);

    ACS_SHORT_LOG((LM_INFO,"spawning workerThread"));
    getComponent()->getThreadManager()->suspend("workerThread");
    ACS_SHORT_LOG((LM_INFO,"suspending workerThread"));


