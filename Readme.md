Object types:

Singletons: 
  Globally accessible via static getter. Used when the class is either lightweigt enough to persist over the whole lifetime or when the program can never run without an instance.
Managed/Shared Singletons: 
  Available over a manager (ResourceConsumerManager, therefore 'managed') that manages lifetime and therefore memory consumption of many managed singletons. Used for resource heavy objects that usually can be used multiple times (using a set() method) and thus can be 'shared' between different places.

