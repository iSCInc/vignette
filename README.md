[![Build Status](https://travis-ci.org/Wikia/vignette.svg?branch=master)](https://travis-ci.org/Wikia/vignette)

# Vignette

A Clojure library for thumbnail generation and storage.

# Development
To run in development:

1. Install [leiningen](http://leiningen.org/)
2. Install [ImageMagick](http://imagemagick.org/)
	* on OSX, with [homebrew](http://brew.sh/): brew install imagemagick --with-webp
3. Set environment variables (see below)
4. From the command line, run ```lein repl```
5. In the REPL, run ```(start system-s3 8080)``` to run using your defined s3 backend,
or ```(start system-local 8080)``` to run using your defined local backend

Vignette should now be running on localhost:8080

## Environment Variables

Below is a list of environment variables that will affect the vignette runtime.

 * `LOGGER_APPLICATION`           this is primarily for wikia-commons. Set this to “vignette”.
 * `LOGGER_TYPE`                  where to log. [file, syslog]
 * `LOGGER_SYSLOG_HOST`           Syslog host:port to log to when LOGGER_TYPE=syslog. [127.0.0.1]
 * `LOGGER_FILE_OUTPUT`           Which file to log to when LOGGER_TYPE=file. [logs/wikia-logger.log]
 * `STORAGE_ACCESS_KEY`           S3 access key
 * `STORAGE_SECRET_KEY`           S3 secret key
 * `STORAGE_ENDPOINT`             S3 HTTP endpoint
 * `STORAGE_PROXY`                S3 Proxy
 * `STORAGE_MAX_RETRIES`          S3 max error retry count; defaults to 0
 * `STORAGE_PROXY_PORT`           S3 Proxy port
 * `VIGNETTE_TEMP_FILE_LOCATION`  temporary file location. This is used for thumbnail generation. [/tmp/vignette]
 * `VIGNETTE_THUMBNAIL_BIN`       path to the thumbnail script [/usr/local/bin/thumbnail, bin/thumbnail]
 * `VIGNETTE_INTEGRATION_ROOT`    path to use for integration testing files [/tmp/integration]
 * `VIGNETTE_SERVER_THREADS`      number of threads to allocate for http-kit [4]
 * `VIGNETTE_SERVER_QUEUE_SIZE`   queue size to allocate for http-kit [20000]
 * `IMAGEMAGICK_BASE`             path to the root of the ImageMagick installation [/usr/local]
 * `GETOPT`                       when running on osx, install gnu-getopt using brew. see bin/thumbnail
 * `CONVERT_CONSTRAINTS`          universal options to pass to ImageMagick. see bin/thumbnail
 * `UNSUPPORTED_REDIRECT_HOST`    on an unsupported legacy thumbnail request, host to redirect

## Testing

All testing is done using [Midje](https://github.com/marick/Midje). Running `lein midje` will run all of the tests.

## Entry point

The main entry point for Vignette happens in src/vignette/http/routes.clj

## Thumbnail Mode Support

For testing an experimentation, vignette includes a facility for setting up integration or browser testing. To set this up,
do the following:

```sh
$ lein repl

; creates the integration environment which just copies some images to /tmp
user=> (i/create-integration-env)

; starts the server using the local integration environment
user=> (start system-local 8080)
```

### Thumbnailing modes

The following examples were rendered from [beach.jpg](/image-samples/beach.jpg)
and [carousel.jpg](/image-samples/carousel.jpg).

#### fixed-aspect-ratio

| beach.jpg                                                         | carousel.jpg |
| :--------                                                         | -----------: |
| ![beach fixed-aspect-ratio](/assets/fixed-aspect-ratio/beach.jpg) | ![carousel
fixed-aspect-ratio](/assets/fixed-aspect-ratio/carousel.jpg) ||
[Fixed Aspect Ratio]



#### fixed-aspect-ratio-down
#### scale-to-width
#### thumbnail
#### thumbnail-down
#### top-crop
#### top-crop-down
#### window-crop
#### window-crop-fixed
#### zoom-crop
#### zoom-crop-down

# License

Copyright © 2014 Wikia

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

# Contributors

 * [Damon Snyder](https://github.com/drsnyder)
 * [Nelson Monterroso](https://github.com/nmonterroso)
