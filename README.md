# play1-base
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Play framework 1.4.x base module for build RESTful api.

# Features

* todo


# Usage
To add a dependency on api module. use the following:
Add it to your ``dependencies.yml``
```yaml
require:
    - play
    - play1-base -> api 0.3.7
repositories:
    - play-api:
          type: http
          artifact: http://pek3a.qingstor.com/playbase/play-api/api-0.3.7.zip
          contains:
            - play1-base -> api
```

To add a dependency on jongo module. use the following:
Add it to your ``dependencies.yml``
```yaml
require:
    - play
    - play1-base -> jongo 0.1
repositories:
    - play-jongo:
          type: http
          artifact: http://pek3a.qingstor.com/playbase/play-jongo/jongo-0.1.zip
          contains:
            - play1-base -> jongo
```


# Examples

### API

TODO



# Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/fivesmallq/play1-base.
