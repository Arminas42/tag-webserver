import React from 'react';
import { Link, useLocation } from 'react-router-dom';

type Tab = {
    path: string;
    label: string;
};

type AppLayoutProps = {
    tabs?: Tab[];
    onLogout?: () => void;
    children: React.ReactNode;
};

const defaultTabs: Tab[] = [
    { path: '/counter', label: 'Counter' },
    { path: '/overview', label: 'Overview' },
];

export function AppLayout({ tabs = defaultTabs, onLogout, children }: AppLayoutProps) {
    const location = useLocation();

    return (
        <div className="min-h-screen bg-background text-foreground">
            <header className="border-b bg-card">
                <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                    <div className="flex h-14 items-center justify-between">
                        <nav className="flex items-center gap-4">
                            {tabs.map((tab) => {
                                const isActive = location.pathname.endsWith(tab.path.replace(/^\//, ''));
                                return (
                                    <Link
                                        key={tab.path}
                                        to={tab.path}
                                        className={
                                            isActive
                                                ? 'text-primary font-medium'
                                                : 'text-muted-foreground hover:text-foreground'
                                        }
                                    >
                                        {tab.label}
                                    </Link>
                                );
                            })}
                        </nav>
                        <div className="flex items-center gap-2">
                            <button
                                type="button"
                                onClick={onLogout}
                                className="inline-flex items-center rounded-md bg-primary px-3 py-1.5 text-sm font-medium text-primary-foreground shadow hover:opacity-90"
                            >
                                Logout
                            </button>
                        </div>
                    </div>
                </div>
            </header>
            <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
                {children}
            </main>
        </div>
    );
}

export default AppLayout;


