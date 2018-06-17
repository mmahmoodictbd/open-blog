FROM ubuntu

MAINTAINER Mossaddeque Mahmood

ENV DEBIAN_FRONTEND noninteractive


RUN apt-get update \
    && apt-get install -y --no-install-recommends \
    apache2 \
    nano \
    curl \
    ca-certificates \
    libapache2-mod-jk

RUN cd /tmp \
    && curl -o /tmp/mod-pagespeed.deb https://dl-ssl.google.com/dl/linux/direct/mod-pagespeed-beta_current_amd64.deb \
    && dpkg -i /tmp/mod-pagespeed.deb \
    && apt-get -f install

RUN a2enmod proxy proxy_http headers expires
RUN service apache2 stop

RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf
COPY vhost-default.conf /etc/apache2/sites-available/000-default.conf

RUN service apache2 start

VOLUME ["/var/log/apache2"]
EXPOSE 80 443

ENTRYPOINT ["apache2ctl", "-D", "FOREGROUND"]
