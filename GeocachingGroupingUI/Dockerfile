FROM nginx:latest AS runtime
LABEL authors="Laci"
WORKDIR /app
COPY dist/geocaching-grouping-ui /usr/share/nginx/html
CMD ["sh","-c", "sed -i s/HOST_URL/$HOST_URL/g /usr/share/nginx/html/assets/config/conf.json; /docker-entrypoint.sh nginx -g 'daemon off;'"]
