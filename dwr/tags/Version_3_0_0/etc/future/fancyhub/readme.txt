
Things we should do to improve things:

- Currently publish loop pruning is done by the hubs. This means that a message
  must travel across the network to be rejected by the remote hub. This is
  quite inefficient. Better would be if pruning was done by the connectors to
  save network traffic

- PubSubHubSynchronizer uses a very simple random number generator to create
  it's unique id. Maybe there is a better way of doing this?

- There are 2 many changes needed for web.xml and dwr.xml. It would be good
  if this was more automatic

- DWR feature: if a remoted bean has application scope then maybe it should be
  created at DWR start time in case the constructor needs to do something?

- What do we do with the ScriptSessionListener feature that we've added to DWR?

- 
