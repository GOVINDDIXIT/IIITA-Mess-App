import datetime

from django.utils import timezone
from django.shortcuts import render
from django.http import HttpResponse
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger

from review.models import ReviewQuestion


def homepage(request):
    reports = []
    date = ReviewQuestion.objects.all().order_by(
        '-timestamp').first().timestamp.date()
    queryset = ReviewQuestion.objects.filter(
        timestamp__date=date, ques_type='DAILY')
    while queryset.exists():
        reports.append(queryset)
        date = date - datetime.timedelta(days=1)
        queryset = ReviewQuestion.objects.filter(
            timestamp__date=date, ques_type='DAILY')
    print(reports)
    page = request.GET.get('page', 1)
    paginator = Paginator(reports, 1)
    try:
        posts = paginator.page(page)
    except PageNotAnInteger:
        posts = paginator.page(1)
    except EmptyPage:
        posts = paginator.page(paginator.num_pages)

    return render(request, 'blog/index.html', {'posts': posts})
