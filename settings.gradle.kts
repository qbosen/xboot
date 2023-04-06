rootProject.name = "xboot"

include ("xboot-shared-model")
include ("xboot-spring-common")
include ("xboot-sortable-util")

include ("xboot-consul-config-util")

include ("xboot-sortable-util:sortable-demo")
include ("diff-match-patch-util")
include ("xboot-configuration-properties-adaptor")
include ("object-differ-util")
include ("extension-field")
include ("extension-field:extension-field-demo")
findProject(":extension-field:extension-field-demo")?.name = "extension-field-demo"
include ("broadcast-message")
include ("broadcast-message:broadcast-message-starter-redis")
findProject(":broadcast-message:broadcast-message-starter-redis")?.name = "broadcast-message-starter-redis"
include ("broadcast-message:broadcast-message-integration-test")
findProject(":broadcast-message:broadcast-message-integration-test")?.name = "broadcast-message-integration-test"
