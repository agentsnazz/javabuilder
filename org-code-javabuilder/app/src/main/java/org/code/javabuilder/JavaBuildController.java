package org.code.javabuilder;

import java.security.Principal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

/**
 * Accepts requests to the /execute channel to compile and run user code. Directs output from the
 * user's program to the user on the /topic/output channel.
 */
@Controller
public class JavaBuildController {

  private final JavaRunner javaRunner;

  JavaBuildController(CompileRunService compileRunService, JavaRunner javaRunner) {
    this.javaRunner = javaRunner;
  }

  /**
   * Executes the user code and sends the output of that code across the established websocket.
   *
   * @param userProgram UserProgram containing users code and file name
   * @param principal the user running the code
   * @return UserProgramOutput
   */
  @MessageMapping(Destinations.EXECUTE_CODE)
  @SendToUser(Destinations.PTP_PREFIX + Destinations.OUTPUT_CHANNEL)
  public UserProgramOutput execute(UserProgram userProgram, Principal principal) {
    // TODO: CSA-48 Handle more than one file
    this.javaRunner.compileAndRunUserProgram(userProgram, principal);
    return new UserProgramOutput("Done!");
  }
}
