import datetime

from django.utils import timezone
from django.shortcuts import render
from django.http import HttpResponse
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger

from review.models import ReviewQuestion


def homepage(request):
    reports = []
    try:
        date = ReviewQuestion.objects.all().order_by(
            '-timestamp').first().timestamp.date()
    except:
        date = False
    queryset = ReviewQuestion.objects.filter(
        timestamp__date=date, ques_type='DAILY')
    while queryset.exists():
        reports.append(queryset)
        date = date - datetime.timedelta(days=1)
        queryset = ReviewQuestion.objects.filter(
            timestamp__date=date, ques_type='DAILY')
    page = request.GET.get('page', 1)
    paginator = Paginator(reports, 10)
    try:
        posts = paginator.page(page)
    except PageNotAnInteger:
        posts = paginator.page(1)
    except EmptyPage:
        posts = paginator.page(paginator.num_pages)

    return render(request, 'blog/index.html', {'posts': posts, 'status': date})


def view_post(request, date):
    report = ReviewQuestion.objects.filter(timestamp__date=date)
    status = report.exists()
    return render(request, 'blog/view_post.html', {'report': report, 'status': status, 'date': date})


def previous_reports(request):
    if request.method == 'POST':
        date = request.POST.get('date')
        queryset = ReviewQuestion.objects.filter(timestamp__date=date)
        return render(request, 'blog/view_post.html', {'report': queryset, 'status': queryset.exists(), 'date': date})
        # except ReviewQuestion.DoesNotExist:
        # return render(request, 'blog/404.html', {'error_msg': f'No Mess Report Found For {date}'})
    return render(request, 'blog/previous_report.html', {'today': timezone.now().date})
