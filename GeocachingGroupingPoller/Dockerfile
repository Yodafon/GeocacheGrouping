FROM python:latest
LABEL authors="Laci"
WORKDIR /app
RUN apt-get update && apt-get -y install cron && rm -rf /var/lib/apt/lists/* /var/cache/apt/archives/*
COPY src/GeocachePoller.py ./
COPY resource/GeocachePollerPROD.properties ./

# Copy hello-cron file to the cron.d directory
COPY resource/PollerCron /etc/cron.d/PollerCron

# Give execution rights on the cron job
RUN chmod 0644 /etc/cron.d/PollerCron

# Apply cron job
RUN crontab /etc/cron.d/PollerCron

RUN touch /var/log/cron.log

# Run the command on container startup
CMD cron && touch /GeocacheGroupingApp/geocachepoller.log && tail -f /GeocacheGroupingApp/geocachepoller.log
