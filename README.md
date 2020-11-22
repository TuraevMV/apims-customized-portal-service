# Naumen [Self-Service Portal](https://www.naumen.ru/products/service_desk/selfservice_portal/)
Изменение списка доступных услуг для отображения на портале


## Решаемая задача
Требуется выводить на портал только услуги, предназначенные для отражения на главной странице. 
В системе такие услуги помечены для отображения в разделе «**популярные**». 
Портал, в базовой конфигурации, так не умеет и выводит на заглавную страницу только первые 12 услуг исходя из индекса популярности. 


## Решение
При загрузке страницы портал посылает запрос вида:
>       https://server/sd/services/portalrest/exec-post?func=modules.portalRest.listUserSlmServices&params=requestContent, user

Важный параметр запроса **func=modules.portalRest.listUserSlmServices**
Так как в системе в качестве прокси web-сервера используется [Nginx](https://nginx.org/ru/),
то у нас есть замечательная возможность перехватить этот запрос:
>      location /sd/ {
>           #перехват потока с портала
>           error_page 420 = @slm_redirect;
>           if ( $arg_func = "modules.portalRest.listUserSlmServices" ) { return 420; }
>           ##########################
>       ....

и направить все в некий микросервис, который будет предоставлять обработанную информацию в необходимом нам составе:
>       location @slm_redirect {
>           return 307 /fakeservice/;
>       }
>       location /fakeservice/ {
>           proxy_pass         http://microservice/getUserServiceList;
>           proxy_set_header   X-Real-IP $remote_addr; 
>           proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
>           proxy_read_timeout 300s;
>       }
>       ###################################

~~С учетом того, что нам доступны заголовки **Authorization Bearer ...**, то мы можем организовать полноценное управление 
предоставлением информации в разрезе конкретного пользователя.~~


Браузер **Safari** внес свои коррективы в картину мира. Он отказывается передавать заголовок авторизации. 
Просто переделал все на обработку cookies в которых есть **access_token**.