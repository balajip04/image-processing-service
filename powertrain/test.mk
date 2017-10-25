# it powertrain config

## Change as per you application need.
## ALl variables below are ',' delimited. If the variable value has a ',' in it, replace it with '!!'
## BIGIP_POOL should be created by the Netinfra team before deploying the app.

## TODO Check with DevOps if this env variable can be used. DT_AGENT_PATH=/apps/Dynatrace/agent/lib64/libdtagent.so=name=social_publishing_gateway1.0!!server=cj8dyl001.cars.com:10001

ENVS=config-api.url=http://composite-it.cars.com/config-api/1.0/rest/config
LABELS=BIGIP_POOL=cars_dockerservice_image_processing_service_it,ENV=it
INSTANCES=1
VOLUMES=/apps/docker/logs/splunkme/:/apps/docker/logs/splunkme/,/apps/Dynatrace/:/apps/Dynatrace/
LOG_DRIVER=splunk
LOG_OPTS=splunk-token=1C327893-A3F4-4B32-8EB3-A685DD4FE4A0,splunk-url=https://splunklog.cars.com:8088,splunk-insecureskipverify=true,tag="{{.ImageName}}!!{{.ID}}",labels=ENV